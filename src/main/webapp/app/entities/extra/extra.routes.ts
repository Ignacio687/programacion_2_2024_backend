import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExtraResolve from './route/extra-routing-resolve.service';

const extraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/extra.component').then(m => m.ExtraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/extra-detail.component').then(m => m.ExtraDetailComponent),
    resolve: {
      extra: ExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/extra-update.component').then(m => m.ExtraUpdateComponent),
    resolve: {
      extra: ExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/extra-update.component').then(m => m.ExtraUpdateComponent),
    resolve: {
      extra: ExtraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extraRoute;
