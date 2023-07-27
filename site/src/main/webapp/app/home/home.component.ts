import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';
import { ImageService } from 'app/entities/image/service/image.service';
import { IImage } from 'app/entities/image/image.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  extendedUser: IExtendedUser | null = null;

  popularImages: IImage[] = [];
  recentImages: IImage[] = [];
  carouselImages: IImage[] = [];

  predicate!: string;
  ascending!: boolean;

  recentTitle = 'global.home.bar.recentImages';
  popularTitle = 'global.home.bar.popularImages';

  activeIndex = 0;

  url = this.imageService.resourceUrl.concat('/base64/');

  MAX_CAROUSEL_IMAGES = 3;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private imageService: ImageService) {
    this.predicate = 'id';
    this.ascending = true;
  }

  trackId = (_index: number, item: IImage): number => this.imageService.getImageIdentifier(item);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.extendedUser = account));

    this.getPopularImages();
    this.getRecentImages();

    // Hacer que el carousel cambie de imagen cada 5 segundos
    setInterval(() => {
      this.nextSlide();
    }
    , 5000);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getPopularImages(): void {
    this.imageService.getPopularImages({
      page: -1,
      size: 20
    }).subscribe((res) => {
      if (!res.body) {
        return;
      }
      this.popularImages = res.body;
      this.carouselImages = this.popularImages.slice(0, this.MAX_CAROUSEL_IMAGES);
      this.carouselImages.sort((a, b) => b.totalLikes! - a.totalLikes!);
      this.carouselImages = this.carouselImages.map((image, index) => ({
        ...image,
        order: index,
      }));
    })
  }

  getRecentImages(): void {
    this.imageService.getRecentImages({
      page: -1,
      size: 20
    }).subscribe((res) => {
      if (res.body) {
        this.recentImages = res.body;
      }
    })
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  onLikeClick(image: IImage): void {
    this.imageService.likeImage(image.id).subscribe(() => {
        this.getPopularImages();
        this.getRecentImages();
    });
  }

  onCommentClick(image: IImage): void {
    this.router.navigate(['/image', image.id, 'view']);
  }

  prevSlide(): void {
    this.activeIndex = (this.activeIndex - 1 + this.carouselImages.length) % this.carouselImages.length;
  }

  nextSlide(): void  {
    this.activeIndex = (this.activeIndex + 1) % this.carouselImages.length;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
