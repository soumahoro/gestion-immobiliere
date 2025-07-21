import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReglement, NewReglement } from '../reglement.model';

export type PartialUpdateReglement = Partial<IReglement> & Pick<IReglement, 'id'>;

type RestOf<T extends IReglement | NewReglement> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestReglement = RestOf<IReglement>;

export type NewRestReglement = RestOf<NewReglement>;

export type PartialUpdateRestReglement = RestOf<PartialUpdateReglement>;

export type EntityResponseType = HttpResponse<IReglement>;
export type EntityArrayResponseType = HttpResponse<IReglement[]>;

@Injectable({ providedIn: 'root' })
export class ReglementService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reglements');

  create(reglement: NewReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .post<RestReglement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reglement: IReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .put<RestReglement>(`${this.resourceUrl}/${this.getReglementIdentifier(reglement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reglement: PartialUpdateReglement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reglement);
    return this.http
      .patch<RestReglement>(`${this.resourceUrl}/${this.getReglementIdentifier(reglement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReglement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReglement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReglementIdentifier(reglement: Pick<IReglement, 'id'>): number {
    return reglement.id;
  }

  compareReglement(o1: Pick<IReglement, 'id'> | null, o2: Pick<IReglement, 'id'> | null): boolean {
    return o1 && o2 ? this.getReglementIdentifier(o1) === this.getReglementIdentifier(o2) : o1 === o2;
  }

  addReglementToCollectionIfMissing<Type extends Pick<IReglement, 'id'>>(
    reglementCollection: Type[],
    ...reglementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reglements: Type[] = reglementsToCheck.filter(isPresent);
    if (reglements.length > 0) {
      const reglementCollectionIdentifiers = reglementCollection.map(reglementItem => this.getReglementIdentifier(reglementItem));
      const reglementsToAdd = reglements.filter(reglementItem => {
        const reglementIdentifier = this.getReglementIdentifier(reglementItem);
        if (reglementCollectionIdentifiers.includes(reglementIdentifier)) {
          return false;
        }
        reglementCollectionIdentifiers.push(reglementIdentifier);
        return true;
      });
      return [...reglementsToAdd, ...reglementCollection];
    }
    return reglementCollection;
  }

  protected convertDateFromClient<T extends IReglement | NewReglement | PartialUpdateReglement>(reglement: T): RestOf<T> {
    return {
      ...reglement,
      date: reglement.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReglement: RestReglement): IReglement {
    return {
      ...restReglement,
      date: restReglement.date ? dayjs(restReglement.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReglement>): HttpResponse<IReglement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReglement[]>): HttpResponse<IReglement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
