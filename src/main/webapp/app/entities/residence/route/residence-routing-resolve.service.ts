import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResidence } from '../residence.model';
import { ResidenceService } from '../service/residence.service';

const residenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IResidence> => {
  const id = route.params.id;
  if (id) {
    return inject(ResidenceService)
      .find(id)
      .pipe(
        mergeMap((residence: HttpResponse<IResidence>) => {
          if (residence.body) {
            return of(residence.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default residenceResolve;
