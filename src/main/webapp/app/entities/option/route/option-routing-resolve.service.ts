import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOption } from '../option.model';
import { OptionService } from '../service/option.service';

const optionResolve = (route: ActivatedRouteSnapshot): Observable<null | IOption> => {
  const id = route.params.id;
  if (id) {
    return inject(OptionService)
      .find(id)
      .pipe(
        mergeMap((option: HttpResponse<IOption>) => {
          if (option.body) {
            return of(option.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default optionResolve;
