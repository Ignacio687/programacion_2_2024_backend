import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SaleItemResolve from './route/sale-item-routing-resolve.service';

const saleItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sale-item.component').then(m => m.SaleItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sale-item-detail.component').then(m => m.SaleItemDetailComponent),
    resolve: {
      saleItem: SaleItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sale-item-update.component').then(m => m.SaleItemUpdateComponent),
    resolve: {
      saleItem: SaleItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sale-item-update.component').then(m => m.SaleItemUpdateComponent),
    resolve: {
      saleItem: SaleItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default saleItemRoute;
