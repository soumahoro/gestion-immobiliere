import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppartement } from '../appartement.model';
import { AppartementService } from '../service/appartement.service';

const appartementResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppartement> => {
  const id = route.params.id;
  if (id) {
    return inject(AppartementService)
      .find(id)
      .pipe(
        mergeMap((appartement: HttpResponse<IAppartement>) => {
          if (appartement.body) {
            return of(appartement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default appartementResolve;
