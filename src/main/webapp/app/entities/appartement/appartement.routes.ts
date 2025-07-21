import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AppartementResolve from './route/appartement-routing-resolve.service';

const appartementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/appartement.component').then(m => m.AppartementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/appartement-detail.component').then(m => m.AppartementDetailComponent),
    resolve: {
      appartement: AppartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/appartement-update.component').then(m => m.AppartementUpdateComponent),
    resolve: {
      appartement: AppartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/appartement-update.component').then(m => m.AppartementUpdateComponent),
    resolve: {
      appartement: AppartementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appartementRoute;
