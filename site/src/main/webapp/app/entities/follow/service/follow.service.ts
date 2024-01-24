import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFollow, NewFollow } from '../follow.model';

export type PartialUpdateFollow = Partial<IFollow> & Pick<IFollow, 'id'>;

type RestOf<T extends IFollow | NewFollow> = Omit<T, 'creationDate' | 'acceptanceDate'> & {
  creationDate?: string | null;
  acceptanceDate?: string | null;
};

export type RestFollow = RestOf<IFollow>;

export type NewRestFollow = RestOf<NewFollow>;

export type PartialUpdateRestFollow = RestOf<PartialUpdateFollow>;

export type EntityResponseType = HttpResponse<IFollow>;
export type EntityArrayResponseType = HttpResponse<IFollow[]>;

@Injectable({ providedIn: 'root' })
export class FollowService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/follows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(follow: NewFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .post<RestFollow>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(follow: IFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .put<RestFollow>(`${this.resourceUrl}/${this.getFollowIdentifier(follow)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(follow: PartialUpdateFollow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(follow);
    return this.http
      .patch<RestFollow>(`${this.resourceUrl}/${this.getFollowIdentifier(follow)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFollow>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFollow[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFollowIdentifier(follow: Pick<IFollow, 'id'>): number {
    return follow.id;
  }

  compareFollow(o1: Pick<IFollow, 'id'> | null, o2: Pick<IFollow, 'id'> | null): boolean {
    return o1 && o2 ? this.getFollowIdentifier(o1) === this.getFollowIdentifier(o2) : o1 === o2;
  }

  addFollowToCollectionIfMissing<Type extends Pick<IFollow, 'id'>>(
    followCollection: Type[],
    ...followsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const follows: Type[] = followsToCheck.filter(isPresent);
    if (follows.length > 0) {
      const followCollectionIdentifiers = followCollection.map(followItem => this.getFollowIdentifier(followItem)!);
      const followsToAdd = follows.filter(followItem => {
        const followIdentifier = this.getFollowIdentifier(followItem);
        if (followCollectionIdentifiers.includes(followIdentifier)) {
          return false;
        }
        followCollectionIdentifiers.push(followIdentifier);
        return true;
      });
      return [...followsToAdd, ...followCollection];
    }
    return followCollection;
  }

  protected convertDateFromClient<T extends IFollow | NewFollow | PartialUpdateFollow>(follow: T): RestOf<T> {
    return {
      ...follow,
      creationDate: follow.creationDate?.toJSON() ?? null,
      acceptanceDate: follow.acceptanceDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFollow: RestFollow): IFollow {
    return {
      ...restFollow,
      creationDate: restFollow.creationDate ? dayjs(restFollow.creationDate) : undefined,
      acceptanceDate: restFollow.acceptanceDate ? dayjs(restFollow.acceptanceDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFollow>): HttpResponse<IFollow> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFollow[]>): HttpResponse<IFollow[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
