import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppartement, NewAppartement } from '../appartement.model';

export type PartialUpdateAppartement = Partial<IAppartement> & Pick<IAppartement, 'id'>;

export type EntityResponseType = HttpResponse<IAppartement>;
export type EntityArrayResponseType = HttpResponse<IAppartement[]>;

@Injectable({ providedIn: 'root' })
export class AppartementService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appartements');

  create(appartement: NewAppartement): Observable<EntityResponseType> {
    return this.http.post<IAppartement>(this.resourceUrl, appartement, { observe: 'response' });
  }

  update(appartement: IAppartement): Observable<EntityResponseType> {
    return this.http.put<IAppartement>(`${this.resourceUrl}/${this.getAppartementIdentifier(appartement)}`, appartement, {
      observe: 'response',
    });
  }

  partialUpdate(appartement: PartialUpdateAppartement): Observable<EntityResponseType> {
    return this.http.patch<IAppartement>(`${this.resourceUrl}/${this.getAppartementIdentifier(appartement)}`, appartement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppartement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppartement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppartementIdentifier(appartement: Pick<IAppartement, 'id'>): number {
    return appartement.id;
  }

  compareAppartement(o1: Pick<IAppartement, 'id'> | null, o2: Pick<IAppartement, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppartementIdentifier(o1) === this.getAppartementIdentifier(o2) : o1 === o2;
  }

  addAppartementToCollectionIfMissing<Type extends Pick<IAppartement, 'id'>>(
    appartementCollection: Type[],
    ...appartementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appartements: Type[] = appartementsToCheck.filter(isPresent);
    if (appartements.length > 0) {
      const appartementCollectionIdentifiers = appartementCollection.map(appartementItem => this.getAppartementIdentifier(appartementItem));
      const appartementsToAdd = appartements.filter(appartementItem => {
        const appartementIdentifier = this.getAppartementIdentifier(appartementItem);
        if (appartementCollectionIdentifiers.includes(appartementIdentifier)) {
          return false;
        }
        appartementCollectionIdentifiers.push(appartementIdentifier);
        return true;
      });
      return [...appartementsToAdd, ...appartementCollection];
    }
    return appartementCollection;
  }
}
