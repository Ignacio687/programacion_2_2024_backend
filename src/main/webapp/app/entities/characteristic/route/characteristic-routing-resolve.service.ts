import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacteristic } from '../characteristic.model';
import { CharacteristicService } from '../service/characteristic.service';

const characteristicResolve = (route: ActivatedRouteSnapshot): Observable<null | ICharacteristic> => {
  const id = route.params.id;
  if (id) {
    return inject(CharacteristicService)
      .find(id)
      .pipe(
        mergeMap((characteristic: HttpResponse<ICharacteristic>) => {
          if (characteristic.body) {
            return of(characteristic.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default characteristicResolve;
