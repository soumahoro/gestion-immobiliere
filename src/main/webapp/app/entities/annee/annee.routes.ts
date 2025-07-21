import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AnneeResolve from './route/annee-routing-resolve.service';

const anneeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/annee.component').then(m => m.AnneeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/annee-detail.component').then(m => m.AnneeDetailComponent),
    resolve: {
      annee: AnneeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/annee-update.component').then(m => m.AnneeUpdateComponent),
    resolve: {
      annee: AnneeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/annee-update.component').then(m => m.AnneeUpdateComponent),
    resolve: {
      annee: AnneeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default anneeRoute;
