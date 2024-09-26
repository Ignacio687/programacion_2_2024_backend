import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExtra, NewExtra } from '../extra.model';

export type PartialUpdateExtra = Partial<IExtra> & Pick<IExtra, 'id'>;

export type EntityResponseType = HttpResponse<IExtra>;
export type EntityArrayResponseType = HttpResponse<IExtra[]>;

@Injectable({ providedIn: 'root' })
export class ExtraService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extras');

  create(extra: NewExtra): Observable<EntityResponseType> {
    return this.http.post<IExtra>(this.resourceUrl, extra, { observe: 'response' });
  }

  update(extra: IExtra): Observable<EntityResponseType> {
    return this.http.put<IExtra>(`${this.resourceUrl}/${this.getExtraIdentifier(extra)}`, extra, { observe: 'response' });
  }

  partialUpdate(extra: PartialUpdateExtra): Observable<EntityResponseType> {
    return this.http.patch<IExtra>(`${this.resourceUrl}/${this.getExtraIdentifier(extra)}`, extra, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExtra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExtra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExtraIdentifier(extra: Pick<IExtra, 'id'>): number {
    return extra.id;
  }

  compareExtra(o1: Pick<IExtra, 'id'> | null, o2: Pick<IExtra, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtraIdentifier(o1) === this.getExtraIdentifier(o2) : o1 === o2;
  }

  addExtraToCollectionIfMissing<Type extends Pick<IExtra, 'id'>>(
    extraCollection: Type[],
    ...extrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extras: Type[] = extrasToCheck.filter(isPresent);
    if (extras.length > 0) {
      const extraCollectionIdentifiers = extraCollection.map(extraItem => this.getExtraIdentifier(extraItem));
      const extrasToAdd = extras.filter(extraItem => {
        const extraIdentifier = this.getExtraIdentifier(extraItem);
        if (extraCollectionIdentifiers.includes(extraIdentifier)) {
          return false;
        }
        extraCollectionIdentifiers.push(extraIdentifier);
        return true;
      });
      return [...extrasToAdd, ...extraCollection];
    }
    return extraCollection;
  }
}
