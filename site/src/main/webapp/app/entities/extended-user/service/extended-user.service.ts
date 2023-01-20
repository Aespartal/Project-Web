import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExtendedUser, NewExtendedUser } from '../extended-user.model';

export type PartialUpdateExtendedUser = Partial<IExtendedUser> & Pick<IExtendedUser, 'id'>;

type RestOf<T extends IExtendedUser | NewExtendedUser> = Omit<T, 'birthDate'> & {
  birthDate?: string | null;
};

export type RestExtendedUser = RestOf<IExtendedUser>;

export type NewRestExtendedUser = RestOf<NewExtendedUser>;

export type PartialUpdateRestExtendedUser = RestOf<PartialUpdateExtendedUser>;

export type EntityResponseType = HttpResponse<IExtendedUser>;
export type EntityArrayResponseType = HttpResponse<IExtendedUser[]>;

@Injectable({ providedIn: 'root' })
export class ExtendedUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extended-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(extendedUser: NewExtendedUser | IExtendedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extendedUser);
    return this.http
      .post<RestExtendedUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  me(): Observable<EntityResponseType> {
    return this.http
      .get<RestExtendedUser>(`${this.resourceUrl}/me`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(extendedUser: IExtendedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extendedUser);
    return this.http
      .put<RestExtendedUser>(`${this.resourceUrl}/${this.getExtendedUserIdentifier(extendedUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(extendedUser: PartialUpdateExtendedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extendedUser);
    return this.http
      .patch<RestExtendedUser>(`${this.resourceUrl}/${this.getExtendedUserIdentifier(extendedUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExtendedUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExtendedUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExtendedUserIdentifier(extendedUser: Pick<IExtendedUser, 'id'>): number {
    return extendedUser.id!;
  }

  compareExtendedUser(o1: Pick<IExtendedUser, 'id'> | null, o2: Pick<IExtendedUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtendedUserIdentifier(o1) === this.getExtendedUserIdentifier(o2) : o1 === o2;
  }

  addExtendedUserToCollectionIfMissing<Type extends Pick<IExtendedUser, 'id'>>(
    extendedUserCollection: Type[],
    ...extendedUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extendedUsers: Type[] = extendedUsersToCheck.filter(isPresent);
    if (extendedUsers.length > 0) {
      const extendedUserCollectionIdentifiers = extendedUserCollection.map(
        extendedUserItem => this.getExtendedUserIdentifier(extendedUserItem)!
      );
      const extendedUsersToAdd = extendedUsers.filter(extendedUserItem => {
        const extendedUserIdentifier = this.getExtendedUserIdentifier(extendedUserItem);
        if (extendedUserCollectionIdentifiers.includes(extendedUserIdentifier)) {
          return false;
        }
        extendedUserCollectionIdentifiers.push(extendedUserIdentifier);
        return true;
      });
      return [...extendedUsersToAdd, ...extendedUserCollection];
    }
    return extendedUserCollection;
  }

  protected convertDateFromClient<T extends IExtendedUser | NewExtendedUser | PartialUpdateExtendedUser>(extendedUser: T): RestOf<T> {
    return {
      ...extendedUser,
      birthDate: extendedUser.birthDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExtendedUser: RestExtendedUser): IExtendedUser {
    return {
      ...restExtendedUser,
      birthDate: restExtendedUser.birthDate ? dayjs(restExtendedUser.birthDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExtendedUser>): HttpResponse<IExtendedUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExtendedUser[]>): HttpResponse<IExtendedUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
