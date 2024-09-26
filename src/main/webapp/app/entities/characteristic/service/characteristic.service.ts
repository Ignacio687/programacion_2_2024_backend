import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharacteristic, NewCharacteristic } from '../characteristic.model';

export type PartialUpdateCharacteristic = Partial<ICharacteristic> & Pick<ICharacteristic, 'id'>;

export type EntityResponseType = HttpResponse<ICharacteristic>;
export type EntityArrayResponseType = HttpResponse<ICharacteristic[]>;

@Injectable({ providedIn: 'root' })
export class CharacteristicService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/characteristics');

  create(characteristic: NewCharacteristic): Observable<EntityResponseType> {
    return this.http.post<ICharacteristic>(this.resourceUrl, characteristic, { observe: 'response' });
  }

  update(characteristic: ICharacteristic): Observable<EntityResponseType> {
    return this.http.put<ICharacteristic>(`${this.resourceUrl}/${this.getCharacteristicIdentifier(characteristic)}`, characteristic, {
      observe: 'response',
    });
  }

  partialUpdate(characteristic: PartialUpdateCharacteristic): Observable<EntityResponseType> {
    return this.http.patch<ICharacteristic>(`${this.resourceUrl}/${this.getCharacteristicIdentifier(characteristic)}`, characteristic, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICharacteristic>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICharacteristic[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCharacteristicIdentifier(characteristic: Pick<ICharacteristic, 'id'>): number {
    return characteristic.id;
  }

  compareCharacteristic(o1: Pick<ICharacteristic, 'id'> | null, o2: Pick<ICharacteristic, 'id'> | null): boolean {
    return o1 && o2 ? this.getCharacteristicIdentifier(o1) === this.getCharacteristicIdentifier(o2) : o1 === o2;
  }

  addCharacteristicToCollectionIfMissing<Type extends Pick<ICharacteristic, 'id'>>(
    characteristicCollection: Type[],
    ...characteristicsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const characteristics: Type[] = characteristicsToCheck.filter(isPresent);
    if (characteristics.length > 0) {
      const characteristicCollectionIdentifiers = characteristicCollection.map(characteristicItem =>
        this.getCharacteristicIdentifier(characteristicItem),
      );
      const characteristicsToAdd = characteristics.filter(characteristicItem => {
        const characteristicIdentifier = this.getCharacteristicIdentifier(characteristicItem);
        if (characteristicCollectionIdentifiers.includes(characteristicIdentifier)) {
          return false;
        }
        characteristicCollectionIdentifiers.push(characteristicIdentifier);
        return true;
      });
      return [...characteristicsToAdd, ...characteristicCollection];
    }
    return characteristicCollection;
  }
}
