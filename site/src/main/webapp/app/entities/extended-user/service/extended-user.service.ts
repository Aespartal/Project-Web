import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExtendedUser, NewExtendedUser } from '../extended-user.model';

export type PartialUpdateExtendedUser = Partial<IExtendedUser> & Pick<IExtendedUser, 'id'>;

export type EntityResponseType = HttpResponse<IExtendedUser>;
export type EntityArrayResponseType = HttpResponse<IExtendedUser[]>;

@Injectable({ providedIn: 'root' })
export class ExtendedUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extended-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(extendedUser: NewExtendedUser): Observable<EntityResponseType> {
    return this.http.post<IExtendedUser>(this.resourceUrl, extendedUser, { observe: 'response' });
  }

  update(extendedUser: IExtendedUser): Observable<EntityResponseType> {
    return this.http.put<IExtendedUser>(`${this.resourceUrl}/${this.getExtendedUserIdentifier(extendedUser)}`, extendedUser, {
      observe: 'response',
    });
  }

  partialUpdate(extendedUser: PartialUpdateExtendedUser): Observable<EntityResponseType> {
    return this.http.patch<IExtendedUser>(`${this.resourceUrl}/${this.getExtendedUserIdentifier(extendedUser)}`, extendedUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExtendedUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExtendedUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExtendedUserIdentifier(extendedUser: Pick<IExtendedUser, 'id'>): number {
    return extendedUser.id;
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
}
