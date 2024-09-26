import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CharacteristicResolve from './route/characteristic-routing-resolve.service';

const characteristicRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/characteristic.component').then(m => m.CharacteristicComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/characteristic-detail.component').then(m => m.CharacteristicDetailComponent),
    resolve: {
      characteristic: CharacteristicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/characteristic-update.component').then(m => m.CharacteristicUpdateComponent),
    resolve: {
      characteristic: CharacteristicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/characteristic-update.component').then(m => m.CharacteristicUpdateComponent),
    resolve: {
      characteristic: CharacteristicResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default characteristicRoute;
