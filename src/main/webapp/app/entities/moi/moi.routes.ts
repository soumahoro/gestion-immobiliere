import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MoiResolve from './route/moi-routing-resolve.service';

const moiRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/moi.component').then(m => m.MoiComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/moi-detail.component').then(m => m.MoiDetailComponent),
    resolve: {
      moi: MoiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/moi-update.component').then(m => m.MoiUpdateComponent),
    resolve: {
      moi: MoiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/moi-update.component').then(m => m.MoiUpdateComponent),
    resolve: {
      moi: MoiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default moiRoute;
