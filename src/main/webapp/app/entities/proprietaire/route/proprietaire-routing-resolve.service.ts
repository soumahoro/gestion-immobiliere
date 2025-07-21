import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProprietaire } from '../proprietaire.model';
import { ProprietaireService } from '../service/proprietaire.service';

const proprietaireResolve = (route: ActivatedRouteSnapshot): Observable<null | IProprietaire> => {
  const id = route.params.id;
  if (id) {
    return inject(ProprietaireService)
      .find(id)
      .pipe(
        mergeMap((proprietaire: HttpResponse<IProprietaire>) => {
          if (proprietaire.body) {
            return of(proprietaire.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default proprietaireResolve;
