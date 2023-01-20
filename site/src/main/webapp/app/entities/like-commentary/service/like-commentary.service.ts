import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILikeCommentary, NewLikeCommentary } from '../like-commentary.model';

export type PartialUpdateLikeCommentary = Partial<ILikeCommentary> & Pick<ILikeCommentary, 'id'>;

type RestOf<T extends ILikeCommentary | NewLikeCommentary> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

export type RestLikeCommentary = RestOf<ILikeCommentary>;

export type NewRestLikeCommentary = RestOf<NewLikeCommentary>;

export type PartialUpdateRestLikeCommentary = RestOf<PartialUpdateLikeCommentary>;

export type EntityResponseType = HttpResponse<ILikeCommentary>;
export type EntityArrayResponseType = HttpResponse<ILikeCommentary[]>;

@Injectable({ providedIn: 'root' })
export class LikeCommentaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/like-commentaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(likeCommentary: NewLikeCommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeCommentary);
    return this.http
      .post<RestLikeCommentary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(likeCommentary: ILikeCommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeCommentary);
    return this.http
      .put<RestLikeCommentary>(`${this.resourceUrl}/${this.getLikeCommentaryIdentifier(likeCommentary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(likeCommentary: PartialUpdateLikeCommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeCommentary);
    return this.http
      .patch<RestLikeCommentary>(`${this.resourceUrl}/${this.getLikeCommentaryIdentifier(likeCommentary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLikeCommentary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLikeCommentary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLikeCommentaryIdentifier(likeCommentary: Pick<ILikeCommentary, 'id'>): number {
    return likeCommentary.id;
  }

  compareLikeCommentary(o1: Pick<ILikeCommentary, 'id'> | null, o2: Pick<ILikeCommentary, 'id'> | null): boolean {
    return o1 && o2 ? this.getLikeCommentaryIdentifier(o1) === this.getLikeCommentaryIdentifier(o2) : o1 === o2;
  }

  addLikeCommentaryToCollectionIfMissing<Type extends Pick<ILikeCommentary, 'id'>>(
    likeCommentaryCollection: Type[],
    ...likeCommentariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const likeCommentaries: Type[] = likeCommentariesToCheck.filter(isPresent);
    if (likeCommentaries.length > 0) {
      const likeCommentaryCollectionIdentifiers = likeCommentaryCollection.map(
        likeCommentaryItem => this.getLikeCommentaryIdentifier(likeCommentaryItem)!
      );
      const likeCommentariesToAdd = likeCommentaries.filter(likeCommentaryItem => {
        const likeCommentaryIdentifier = this.getLikeCommentaryIdentifier(likeCommentaryItem);
        if (likeCommentaryCollectionIdentifiers.includes(likeCommentaryIdentifier)) {
          return false;
        }
        likeCommentaryCollectionIdentifiers.push(likeCommentaryIdentifier);
        return true;
      });
      return [...likeCommentariesToAdd, ...likeCommentaryCollection];
    }
    return likeCommentaryCollection;
  }

  protected convertDateFromClient<T extends ILikeCommentary | NewLikeCommentary | PartialUpdateLikeCommentary>(
    likeCommentary: T
  ): RestOf<T> {
    return {
      ...likeCommentary,
      creationDate: likeCommentary.creationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLikeCommentary: RestLikeCommentary): ILikeCommentary {
    return {
      ...restLikeCommentary,
      creationDate: restLikeCommentary.creationDate ? dayjs(restLikeCommentary.creationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLikeCommentary>): HttpResponse<ILikeCommentary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLikeCommentary[]>): HttpResponse<ILikeCommentary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
