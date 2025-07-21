import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdministrateurResolve from './route/administrateur-routing-resolve.service';

const administrateurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/administrateur.component').then(m => m.AdministrateurComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/administrateur-detail.component').then(m => m.AdministrateurDetailComponent),
    resolve: {
      administrateur: AdministrateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/administrateur-update.component').then(m => m.AdministrateurUpdateComponent),
    resolve: {
      administrateur: AdministrateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/administrateur-update.component').then(m => m.AdministrateurUpdateComponent),
    resolve: {
      administrateur: AdministrateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default administrateurRoute;
