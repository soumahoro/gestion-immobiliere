import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrateur, NewAdministrateur } from '../administrateur.model';

export type PartialUpdateAdministrateur = Partial<IAdministrateur> & Pick<IAdministrateur, 'id'>;

export type EntityResponseType = HttpResponse<IAdministrateur>;
export type EntityArrayResponseType = HttpResponse<IAdministrateur[]>;

@Injectable({ providedIn: 'root' })
export class AdministrateurService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/administrateurs');

  create(administrateur: NewAdministrateur): Observable<EntityResponseType> {
    return this.http.post<IAdministrateur>(this.resourceUrl, administrateur, { observe: 'response' });
  }

  update(administrateur: IAdministrateur): Observable<EntityResponseType> {
    return this.http.put<IAdministrateur>(`${this.resourceUrl}/${this.getAdministrateurIdentifier(administrateur)}`, administrateur, {
      observe: 'response',
    });
  }

  partialUpdate(administrateur: PartialUpdateAdministrateur): Observable<EntityResponseType> {
    return this.http.patch<IAdministrateur>(`${this.resourceUrl}/${this.getAdministrateurIdentifier(administrateur)}`, administrateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdministrateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdministrateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdministrateurIdentifier(administrateur: Pick<IAdministrateur, 'id'>): number {
    return administrateur.id;
  }

  compareAdministrateur(o1: Pick<IAdministrateur, 'id'> | null, o2: Pick<IAdministrateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdministrateurIdentifier(o1) === this.getAdministrateurIdentifier(o2) : o1 === o2;
  }

  addAdministrateurToCollectionIfMissing<Type extends Pick<IAdministrateur, 'id'>>(
    administrateurCollection: Type[],
    ...administrateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const administrateurs: Type[] = administrateursToCheck.filter(isPresent);
    if (administrateurs.length > 0) {
      const administrateurCollectionIdentifiers = administrateurCollection.map(administrateurItem =>
        this.getAdministrateurIdentifier(administrateurItem),
      );
      const administrateursToAdd = administrateurs.filter(administrateurItem => {
        const administrateurIdentifier = this.getAdministrateurIdentifier(administrateurItem);
        if (administrateurCollectionIdentifiers.includes(administrateurIdentifier)) {
          return false;
        }
        administrateurCollectionIdentifiers.push(administrateurIdentifier);
        return true;
      });
      return [...administrateursToAdd, ...administrateurCollection];
    }
    return administrateurCollection;
  }
}
