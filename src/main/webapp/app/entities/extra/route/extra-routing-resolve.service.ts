import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtra } from '../extra.model';
import { ExtraService } from '../service/extra.service';

const extraResolve = (route: ActivatedRouteSnapshot): Observable<null | IExtra> => {
  const id = route.params.id;
  if (id) {
    return inject(ExtraService)
      .find(id)
      .pipe(
        mergeMap((extra: HttpResponse<IExtra>) => {
          if (extra.body) {
            return of(extra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default extraResolve;
