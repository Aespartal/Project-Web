import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILikeImage, NewLikeImage } from '../like-image.model';

export type PartialUpdateLikeImage = Partial<ILikeImage> & Pick<ILikeImage, 'id'>;

type RestOf<T extends ILikeImage | NewLikeImage> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

export type RestLikeImage = RestOf<ILikeImage>;

export type NewRestLikeImage = RestOf<NewLikeImage>;

export type PartialUpdateRestLikeImage = RestOf<PartialUpdateLikeImage>;

export type EntityResponseType = HttpResponse<ILikeImage>;
export type EntityArrayResponseType = HttpResponse<ILikeImage[]>;

@Injectable({ providedIn: 'root' })
export class LikeImageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/like-images');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(likeImage: NewLikeImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeImage);
    return this.http
      .post<RestLikeImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(likeImage: ILikeImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeImage);
    return this.http
      .put<RestLikeImage>(`${this.resourceUrl}/${this.getLikeImageIdentifier(likeImage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(likeImage: PartialUpdateLikeImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(likeImage);
    return this.http
      .patch<RestLikeImage>(`${this.resourceUrl}/${this.getLikeImageIdentifier(likeImage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLikeImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLikeImage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLikeImageIdentifier(likeImage: Pick<ILikeImage, 'id'>): number {
    return likeImage.id;
  }

  compareLikeImage(o1: Pick<ILikeImage, 'id'> | null, o2: Pick<ILikeImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getLikeImageIdentifier(o1) === this.getLikeImageIdentifier(o2) : o1 === o2;
  }

  addLikeImageToCollectionIfMissing<Type extends Pick<ILikeImage, 'id'>>(
    likeImageCollection: Type[],
    ...likeImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const likeImages: Type[] = likeImagesToCheck.filter(isPresent);
    if (likeImages.length > 0) {
      const likeImageCollectionIdentifiers = likeImageCollection.map(likeImageItem => this.getLikeImageIdentifier(likeImageItem)!);
      const likeImagesToAdd = likeImages.filter(likeImageItem => {
        const likeImageIdentifier = this.getLikeImageIdentifier(likeImageItem);
        if (likeImageCollectionIdentifiers.includes(likeImageIdentifier)) {
          return false;
        }
        likeImageCollectionIdentifiers.push(likeImageIdentifier);
        return true;
      });
      return [...likeImagesToAdd, ...likeImageCollection];
    }
    return likeImageCollection;
  }

  protected convertDateFromClient<T extends ILikeImage | NewLikeImage | PartialUpdateLikeImage>(likeImage: T): RestOf<T> {
    return {
      ...likeImage,
      creationDate: likeImage.creationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLikeImage: RestLikeImage): ILikeImage {
    return {
      ...restLikeImage,
      creationDate: restLikeImage.creationDate ? dayjs(restLikeImage.creationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLikeImage>): HttpResponse<ILikeImage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLikeImage[]>): HttpResponse<ILikeImage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
