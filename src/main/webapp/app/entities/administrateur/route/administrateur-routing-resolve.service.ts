import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdministrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';

const administrateurResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdministrateur> => {
  const id = route.params.id;
  if (id) {
    return inject(AdministrateurService)
      .find(id)
      .pipe(
        mergeMap((administrateur: HttpResponse<IAdministrateur>) => {
          if (administrateur.body) {
            return of(administrateur.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default administrateurResolve;
