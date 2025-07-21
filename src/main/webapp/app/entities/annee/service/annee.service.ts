import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnee, NewAnnee } from '../annee.model';

export type PartialUpdateAnnee = Partial<IAnnee> & Pick<IAnnee, 'id'>;

export type EntityResponseType = HttpResponse<IAnnee>;
export type EntityArrayResponseType = HttpResponse<IAnnee[]>;

@Injectable({ providedIn: 'root' })
export class AnneeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annees');

  create(annee: NewAnnee): Observable<EntityResponseType> {
    return this.http.post<IAnnee>(this.resourceUrl, annee, { observe: 'response' });
  }

  update(annee: IAnnee): Observable<EntityResponseType> {
    return this.http.put<IAnnee>(`${this.resourceUrl}/${this.getAnneeIdentifier(annee)}`, annee, { observe: 'response' });
  }

  partialUpdate(annee: PartialUpdateAnnee): Observable<EntityResponseType> {
    return this.http.patch<IAnnee>(`${this.resourceUrl}/${this.getAnneeIdentifier(annee)}`, annee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnnee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAnneeIdentifier(annee: Pick<IAnnee, 'id'>): number {
    return annee.id;
  }

  compareAnnee(o1: Pick<IAnnee, 'id'> | null, o2: Pick<IAnnee, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnneeIdentifier(o1) === this.getAnneeIdentifier(o2) : o1 === o2;
  }

  addAnneeToCollectionIfMissing<Type extends Pick<IAnnee, 'id'>>(
    anneeCollection: Type[],
    ...anneesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const annees: Type[] = anneesToCheck.filter(isPresent);
    if (annees.length > 0) {
      const anneeCollectionIdentifiers = anneeCollection.map(anneeItem => this.getAnneeIdentifier(anneeItem));
      const anneesToAdd = annees.filter(anneeItem => {
        const anneeIdentifier = this.getAnneeIdentifier(anneeItem);
        if (anneeCollectionIdentifiers.includes(anneeIdentifier)) {
          return false;
        }
        anneeCollectionIdentifiers.push(anneeIdentifier);
        return true;
      });
      return [...anneesToAdd, ...anneeCollection];
    }
    return anneeCollection;
  }
}
