import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISaleItem } from '../sale-item.model';
import { SaleItemService } from '../service/sale-item.service';

const saleItemResolve = (route: ActivatedRouteSnapshot): Observable<null | ISaleItem> => {
  const id = route.params.id;
  if (id) {
    return inject(SaleItemService)
      .find(id)
      .pipe(
        mergeMap((saleItem: HttpResponse<ISaleItem>) => {
          if (saleItem.body) {
            return of(saleItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default saleItemResolve;
