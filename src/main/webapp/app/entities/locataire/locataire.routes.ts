import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LocataireResolve from './route/locataire-routing-resolve.service';

const locataireRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/locataire.component').then(m => m.LocataireComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/locataire-detail.component').then(m => m.LocataireDetailComponent),
    resolve: {
      locataire: LocataireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/locataire-update.component').then(m => m.LocataireUpdateComponent),
    resolve: {
      locataire: LocataireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/locataire-update.component').then(m => m.LocataireUpdateComponent),
    resolve: {
      locataire: LocataireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default locataireRoute;
