import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocataire, NewLocataire } from '../locataire.model';

export type PartialUpdateLocataire = Partial<ILocataire> & Pick<ILocataire, 'id'>;

type RestOf<T extends ILocataire | NewLocataire> = Omit<T, 'date' | 'datedepart'> & {
  date?: string | null;
  datedepart?: string | null;
};

export type RestLocataire = RestOf<ILocataire>;

export type NewRestLocataire = RestOf<NewLocataire>;

export type PartialUpdateRestLocataire = RestOf<PartialUpdateLocataire>;

export type EntityResponseType = HttpResponse<ILocataire>;
export type EntityArrayResponseType = HttpResponse<ILocataire[]>;

@Injectable({ providedIn: 'root' })
export class LocataireService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/locataires');

  create(locataire: NewLocataire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locataire);
    return this.http
      .post<RestLocataire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(locataire: ILocataire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locataire);
    return this.http
      .put<RestLocataire>(`${this.resourceUrl}/${this.getLocataireIdentifier(locataire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(locataire: PartialUpdateLocataire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locataire);
    return this.http
      .patch<RestLocataire>(`${this.resourceUrl}/${this.getLocataireIdentifier(locataire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLocataire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLocataire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocataireIdentifier(locataire: Pick<ILocataire, 'id'>): number {
    return locataire.id;
  }

  compareLocataire(o1: Pick<ILocataire, 'id'> | null, o2: Pick<ILocataire, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocataireIdentifier(o1) === this.getLocataireIdentifier(o2) : o1 === o2;
  }

  addLocataireToCollectionIfMissing<Type extends Pick<ILocataire, 'id'>>(
    locataireCollection: Type[],
    ...locatairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const locataires: Type[] = locatairesToCheck.filter(isPresent);
    if (locataires.length > 0) {
      const locataireCollectionIdentifiers = locataireCollection.map(locataireItem => this.getLocataireIdentifier(locataireItem));
      const locatairesToAdd = locataires.filter(locataireItem => {
        const locataireIdentifier = this.getLocataireIdentifier(locataireItem);
        if (locataireCollectionIdentifiers.includes(locataireIdentifier)) {
          return false;
        }
        locataireCollectionIdentifiers.push(locataireIdentifier);
        return true;
      });
      return [...locatairesToAdd, ...locataireCollection];
    }
    return locataireCollection;
  }

  protected convertDateFromClient<T extends ILocataire | NewLocataire | PartialUpdateLocataire>(locataire: T): RestOf<T> {
    return {
      ...locataire,
      date: locataire.date?.toJSON() ?? null,
      datedepart: locataire.datedepart?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLocataire: RestLocataire): ILocataire {
    return {
      ...restLocataire,
      date: restLocataire.date ? dayjs(restLocataire.date) : undefined,
      datedepart: restLocataire.datedepart ? dayjs(restLocataire.datedepart) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLocataire>): HttpResponse<ILocataire> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLocataire[]>): HttpResponse<ILocataire[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
