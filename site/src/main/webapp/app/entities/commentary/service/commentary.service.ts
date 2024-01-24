import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommentary, NewCommentary } from '../commentary.model';

export type PartialUpdateCommentary = Partial<ICommentary> & Pick<ICommentary, 'id'>;

type RestOf<T extends ICommentary | NewCommentary> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

export type RestCommentary = RestOf<ICommentary>;

export type NewRestCommentary = RestOf<NewCommentary>;

export type PartialUpdateRestCommentary = RestOf<PartialUpdateCommentary>;

export type EntityResponseType = HttpResponse<ICommentary>;
export type EntityArrayResponseType = HttpResponse<ICommentary[]>;

@Injectable({ providedIn: 'root' })
export class CommentaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commentaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commentary: NewCommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentary);
    return this.http
      .post<RestCommentary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(commentary: ICommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentary);
    return this.http
      .put<RestCommentary>(`${this.resourceUrl}/${this.getCommentaryIdentifier(commentary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(commentary: PartialUpdateCommentary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentary);
    return this.http
      .patch<RestCommentary>(`${this.resourceUrl}/${this.getCommentaryIdentifier(commentary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCommentary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCommentary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommentaryIdentifier(commentary: Pick<ICommentary, 'id'>): number {
    return commentary.id;
  }

  compareCommentary(o1: Pick<ICommentary, 'id'> | null, o2: Pick<ICommentary, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommentaryIdentifier(o1) === this.getCommentaryIdentifier(o2) : o1 === o2;
  }

  addCommentaryToCollectionIfMissing<Type extends Pick<ICommentary, 'id'>>(
    commentaryCollection: Type[],
    ...commentariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commentaries: Type[] = commentariesToCheck.filter(isPresent);
    if (commentaries.length > 0) {
      const commentaryCollectionIdentifiers = commentaryCollection.map(commentaryItem => this.getCommentaryIdentifier(commentaryItem)!);
      const commentariesToAdd = commentaries.filter(commentaryItem => {
        const commentaryIdentifier = this.getCommentaryIdentifier(commentaryItem);
        if (commentaryCollectionIdentifiers.includes(commentaryIdentifier)) {
          return false;
        }
        commentaryCollectionIdentifiers.push(commentaryIdentifier);
        return true;
      });
      return [...commentariesToAdd, ...commentaryCollection];
    }
    return commentaryCollection;
  }

  protected convertDateFromClient<T extends ICommentary | NewCommentary | PartialUpdateCommentary>(commentary: T): RestOf<T> {
    return {
      ...commentary,
      creationDate: commentary.creationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCommentary: RestCommentary): ICommentary {
    return {
      ...restCommentary,
      creationDate: restCommentary.creationDate ? dayjs(restCommentary.creationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCommentary>): HttpResponse<ICommentary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCommentary[]>): HttpResponse<ICommentary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
