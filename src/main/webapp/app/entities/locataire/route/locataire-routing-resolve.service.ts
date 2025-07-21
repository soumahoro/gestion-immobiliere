import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocataire } from '../locataire.model';
import { LocataireService } from '../service/locataire.service';

const locataireResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocataire> => {
  const id = route.params.id;
  if (id) {
    return inject(LocataireService)
      .find(id)
      .pipe(
        mergeMap((locataire: HttpResponse<ILocataire>) => {
          if (locataire.body) {
            return of(locataire.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default locataireResolve;
