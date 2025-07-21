import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResidence, NewResidence } from '../residence.model';

export type PartialUpdateResidence = Partial<IResidence> & Pick<IResidence, 'id'>;

export type EntityResponseType = HttpResponse<IResidence>;
export type EntityArrayResponseType = HttpResponse<IResidence[]>;

@Injectable({ providedIn: 'root' })
export class ResidenceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/residences');

  create(residence: NewResidence): Observable<EntityResponseType> {
    return this.http.post<IResidence>(this.resourceUrl, residence, { observe: 'response' });
  }

  update(residence: IResidence): Observable<EntityResponseType> {
    return this.http.put<IResidence>(`${this.resourceUrl}/${this.getResidenceIdentifier(residence)}`, residence, { observe: 'response' });
  }

  partialUpdate(residence: PartialUpdateResidence): Observable<EntityResponseType> {
    return this.http.patch<IResidence>(`${this.resourceUrl}/${this.getResidenceIdentifier(residence)}`, residence, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResidence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResidence[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResidenceIdentifier(residence: Pick<IResidence, 'id'>): number {
    return residence.id;
  }

  compareResidence(o1: Pick<IResidence, 'id'> | null, o2: Pick<IResidence, 'id'> | null): boolean {
    return o1 && o2 ? this.getResidenceIdentifier(o1) === this.getResidenceIdentifier(o2) : o1 === o2;
  }

  addResidenceToCollectionIfMissing<Type extends Pick<IResidence, 'id'>>(
    residenceCollection: Type[],
    ...residencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const residences: Type[] = residencesToCheck.filter(isPresent);
    if (residences.length > 0) {
      const residenceCollectionIdentifiers = residenceCollection.map(residenceItem => this.getResidenceIdentifier(residenceItem));
      const residencesToAdd = residences.filter(residenceItem => {
        const residenceIdentifier = this.getResidenceIdentifier(residenceItem);
        if (residenceCollectionIdentifiers.includes(residenceIdentifier)) {
          return false;
        }
        residenceCollectionIdentifiers.push(residenceIdentifier);
        return true;
      });
      return [...residencesToAdd, ...residenceCollection];
    }
    return residenceCollection;
  }
}
