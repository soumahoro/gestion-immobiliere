import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ResidenceResolve from './route/residence-routing-resolve.service';

const residenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/residence.component').then(m => m.ResidenceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/residence-detail.component').then(m => m.ResidenceDetailComponent),
    resolve: {
      residence: ResidenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/residence-update.component').then(m => m.ResidenceUpdateComponent),
    resolve: {
      residence: ResidenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/residence-update.component').then(m => m.ResidenceUpdateComponent),
    resolve: {
      residence: ResidenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default residenceRoute;
