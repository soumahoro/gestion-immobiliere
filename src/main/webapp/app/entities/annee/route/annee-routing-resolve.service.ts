import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnee } from '../annee.model';
import { AnneeService } from '../service/annee.service';

const anneeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnnee> => {
  const id = route.params.id;
  if (id) {
    return inject(AnneeService)
      .find(id)
      .pipe(
        mergeMap((annee: HttpResponse<IAnnee>) => {
          if (annee.body) {
            return of(annee.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default anneeResolve;
