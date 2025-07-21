import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProprietaire, NewProprietaire } from '../proprietaire.model';

export type PartialUpdateProprietaire = Partial<IProprietaire> & Pick<IProprietaire, 'id'>;

type RestOf<T extends IProprietaire | NewProprietaire> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestProprietaire = RestOf<IProprietaire>;

export type NewRestProprietaire = RestOf<NewProprietaire>;

export type PartialUpdateRestProprietaire = RestOf<PartialUpdateProprietaire>;

export type EntityResponseType = HttpResponse<IProprietaire>;
export type EntityArrayResponseType = HttpResponse<IProprietaire[]>;

@Injectable({ providedIn: 'root' })
export class ProprietaireService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proprietaires');

  create(proprietaire: NewProprietaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proprietaire);
    return this.http
      .post<RestProprietaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(proprietaire: IProprietaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proprietaire);
    return this.http
      .put<RestProprietaire>(`${this.resourceUrl}/${this.getProprietaireIdentifier(proprietaire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(proprietaire: PartialUpdateProprietaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proprietaire);
    return this.http
      .patch<RestProprietaire>(`${this.resourceUrl}/${this.getProprietaireIdentifier(proprietaire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProprietaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProprietaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProprietaireIdentifier(proprietaire: Pick<IProprietaire, 'id'>): number {
    return proprietaire.id;
  }

  compareProprietaire(o1: Pick<IProprietaire, 'id'> | null, o2: Pick<IProprietaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getProprietaireIdentifier(o1) === this.getProprietaireIdentifier(o2) : o1 === o2;
  }

  addProprietaireToCollectionIfMissing<Type extends Pick<IProprietaire, 'id'>>(
    proprietaireCollection: Type[],
    ...proprietairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const proprietaires: Type[] = proprietairesToCheck.filter(isPresent);
    if (proprietaires.length > 0) {
      const proprietaireCollectionIdentifiers = proprietaireCollection.map(proprietaireItem =>
        this.getProprietaireIdentifier(proprietaireItem),
      );
      const proprietairesToAdd = proprietaires.filter(proprietaireItem => {
        const proprietaireIdentifier = this.getProprietaireIdentifier(proprietaireItem);
        if (proprietaireCollectionIdentifiers.includes(proprietaireIdentifier)) {
          return false;
        }
        proprietaireCollectionIdentifiers.push(proprietaireIdentifier);
        return true;
      });
      return [...proprietairesToAdd, ...proprietaireCollection];
    }
    return proprietaireCollection;
  }

  protected convertDateFromClient<T extends IProprietaire | NewProprietaire | PartialUpdateProprietaire>(proprietaire: T): RestOf<T> {
    return {
      ...proprietaire,
      date: proprietaire.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProprietaire: RestProprietaire): IProprietaire {
    return {
      ...restProprietaire,
      date: restProprietaire.date ? dayjs(restProprietaire.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProprietaire>): HttpResponse<IProprietaire> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProprietaire[]>): HttpResponse<IProprietaire[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
