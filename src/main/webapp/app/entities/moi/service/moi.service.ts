import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoi, NewMoi } from '../moi.model';

export type PartialUpdateMoi = Partial<IMoi> & Pick<IMoi, 'id'>;

export type EntityResponseType = HttpResponse<IMoi>;
export type EntityArrayResponseType = HttpResponse<IMoi[]>;

@Injectable({ providedIn: 'root' })
export class MoiService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mois');

  create(moi: NewMoi): Observable<EntityResponseType> {
    return this.http.post<IMoi>(this.resourceUrl, moi, { observe: 'response' });
  }

  update(moi: IMoi): Observable<EntityResponseType> {
    return this.http.put<IMoi>(`${this.resourceUrl}/${this.getMoiIdentifier(moi)}`, moi, { observe: 'response' });
  }

  partialUpdate(moi: PartialUpdateMoi): Observable<EntityResponseType> {
    return this.http.patch<IMoi>(`${this.resourceUrl}/${this.getMoiIdentifier(moi)}`, moi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMoiIdentifier(moi: Pick<IMoi, 'id'>): number {
    return moi.id;
  }

  compareMoi(o1: Pick<IMoi, 'id'> | null, o2: Pick<IMoi, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoiIdentifier(o1) === this.getMoiIdentifier(o2) : o1 === o2;
  }

  addMoiToCollectionIfMissing<Type extends Pick<IMoi, 'id'>>(moiCollection: Type[], ...moisToCheck: (Type | null | undefined)[]): Type[] {
    const mois: Type[] = moisToCheck.filter(isPresent);
    if (mois.length > 0) {
      const moiCollectionIdentifiers = moiCollection.map(moiItem => this.getMoiIdentifier(moiItem));
      const moisToAdd = mois.filter(moiItem => {
        const moiIdentifier = this.getMoiIdentifier(moiItem);
        if (moiCollectionIdentifiers.includes(moiIdentifier)) {
          return false;
        }
        moiCollectionIdentifiers.push(moiIdentifier);
        return true;
      });
      return [...moisToAdd, ...moiCollection];
    }
    return moiCollection;
  }
}
