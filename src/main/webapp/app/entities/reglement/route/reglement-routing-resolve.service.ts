import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReglement } from '../reglement.model';
import { ReglementService } from '../service/reglement.service';

const reglementResolve = (route: ActivatedRouteSnapshot): Observable<null | IReglement> => {
  const id = route.params.id;
  if (id) {
    return inject(ReglementService)
      .find(id)
      .pipe(
        mergeMap((reglement: HttpResponse<IReglement>) => {
          if (reglement.body) {
            return of(reglement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reglementResolve;
