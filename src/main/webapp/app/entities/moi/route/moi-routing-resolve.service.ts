import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoi } from '../moi.model';
import { MoiService } from '../service/moi.service';

const moiResolve = (route: ActivatedRouteSnapshot): Observable<null | IMoi> => {
  const id = route.params.id;
  if (id) {
    return inject(MoiService)
      .find(id)
      .pipe(
        mergeMap((moi: HttpResponse<IMoi>) => {
          if (moi.body) {
            return of(moi.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default moiResolve;
