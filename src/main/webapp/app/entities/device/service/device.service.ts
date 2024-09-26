import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDevice, NewDevice } from '../device.model';

export type PartialUpdateDevice = Partial<IDevice> & Pick<IDevice, 'id'>;

export type EntityResponseType = HttpResponse<IDevice>;
export type EntityArrayResponseType = HttpResponse<IDevice[]>;

@Injectable({ providedIn: 'root' })
export class DeviceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/devices');

  create(device: NewDevice): Observable<EntityResponseType> {
    return this.http.post<IDevice>(this.resourceUrl, device, { observe: 'response' });
  }

  update(device: IDevice): Observable<EntityResponseType> {
    return this.http.put<IDevice>(`${this.resourceUrl}/${this.getDeviceIdentifier(device)}`, device, { observe: 'response' });
  }

  partialUpdate(device: PartialUpdateDevice): Observable<EntityResponseType> {
    return this.http.patch<IDevice>(`${this.resourceUrl}/${this.getDeviceIdentifier(device)}`, device, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeviceIdentifier(device: Pick<IDevice, 'id'>): number {
    return device.id;
  }

  compareDevice(o1: Pick<IDevice, 'id'> | null, o2: Pick<IDevice, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeviceIdentifier(o1) === this.getDeviceIdentifier(o2) : o1 === o2;
  }

  addDeviceToCollectionIfMissing<Type extends Pick<IDevice, 'id'>>(
    deviceCollection: Type[],
    ...devicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const devices: Type[] = devicesToCheck.filter(isPresent);
    if (devices.length > 0) {
      const deviceCollectionIdentifiers = deviceCollection.map(deviceItem => this.getDeviceIdentifier(deviceItem));
      const devicesToAdd = devices.filter(deviceItem => {
        const deviceIdentifier = this.getDeviceIdentifier(deviceItem);
        if (deviceCollectionIdentifiers.includes(deviceIdentifier)) {
          return false;
        }
        deviceCollectionIdentifiers.push(deviceIdentifier);
        return true;
      });
      return [...devicesToAdd, ...deviceCollection];
    }
    return deviceCollection;
  }
}
