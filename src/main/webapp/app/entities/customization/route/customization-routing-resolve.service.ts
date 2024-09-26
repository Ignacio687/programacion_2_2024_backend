import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomization } from '../customization.model';
import { CustomizationService } from '../service/customization.service';

const customizationResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomization> => {
  const id = route.params.id;
  if (id) {
    return inject(CustomizationService)
      .find(id)
      .pipe(
        mergeMap((customization: HttpResponse<ICustomization>) => {
          if (customization.body) {
            return of(customization.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default customizationResolve;
