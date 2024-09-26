import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomization, NewCustomization } from '../customization.model';

export type PartialUpdateCustomization = Partial<ICustomization> & Pick<ICustomization, 'id'>;

export type EntityResponseType = HttpResponse<ICustomization>;
export type EntityArrayResponseType = HttpResponse<ICustomization[]>;

@Injectable({ providedIn: 'root' })
export class CustomizationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customizations');

  create(customization: NewCustomization): Observable<EntityResponseType> {
    return this.http.post<ICustomization>(this.resourceUrl, customization, { observe: 'response' });
  }

  update(customization: ICustomization): Observable<EntityResponseType> {
    return this.http.put<ICustomization>(`${this.resourceUrl}/${this.getCustomizationIdentifier(customization)}`, customization, {
      observe: 'response',
    });
  }

  partialUpdate(customization: PartialUpdateCustomization): Observable<EntityResponseType> {
    return this.http.patch<ICustomization>(`${this.resourceUrl}/${this.getCustomizationIdentifier(customization)}`, customization, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomizationIdentifier(customization: Pick<ICustomization, 'id'>): number {
    return customization.id;
  }

  compareCustomization(o1: Pick<ICustomization, 'id'> | null, o2: Pick<ICustomization, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomizationIdentifier(o1) === this.getCustomizationIdentifier(o2) : o1 === o2;
  }

  addCustomizationToCollectionIfMissing<Type extends Pick<ICustomization, 'id'>>(
    customizationCollection: Type[],
    ...customizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customizations: Type[] = customizationsToCheck.filter(isPresent);
    if (customizations.length > 0) {
      const customizationCollectionIdentifiers = customizationCollection.map(customizationItem =>
        this.getCustomizationIdentifier(customizationItem),
      );
      const customizationsToAdd = customizations.filter(customizationItem => {
        const customizationIdentifier = this.getCustomizationIdentifier(customizationItem);
        if (customizationCollectionIdentifiers.includes(customizationIdentifier)) {
          return false;
        }
        customizationCollectionIdentifiers.push(customizationIdentifier);
        return true;
      });
      return [...customizationsToAdd, ...customizationCollection];
    }
    return customizationCollection;
  }
}
