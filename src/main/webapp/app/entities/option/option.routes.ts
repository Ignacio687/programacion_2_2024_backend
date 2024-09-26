import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OptionResolve from './route/option-routing-resolve.service';

const optionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/option.component').then(m => m.OptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/option-detail.component').then(m => m.OptionDetailComponent),
    resolve: {
      option: OptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/option-update.component').then(m => m.OptionUpdateComponent),
    resolve: {
      option: OptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/option-update.component').then(m => m.OptionUpdateComponent),
    resolve: {
      option: OptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default optionRoute;
