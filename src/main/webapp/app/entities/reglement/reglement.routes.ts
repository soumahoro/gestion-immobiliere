import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReglementResolve from './route/reglement-routing-resolve.service';

const reglementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reglement.component').then(m => m.ReglementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reglement-detail.component').then(m => m.ReglementDetailComponent),
    resolve: {
      reglement: ReglementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reglement-update.component').then(m => m.ReglementUpdateComponent),
    resolve: {
      reglement: ReglementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reglement-update.component').then(m => m.ReglementUpdateComponent),
    resolve: {
      reglement: ReglementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reglementRoute;
