import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AccountService } from "app/core/auth/account.service";
import { IExtendedUser } from "app/entities/extended-user/extended-user.model";
import { IImage } from "app/entities/image/image.model";
import { ImageService } from "app/entities/image/service/image.service";

@Component({
  selector: 'jhi-gallery-content',
  templateUrl: './gallery-content.component.html',
  styleUrls: ['./gallery-content.component.scss'],
})
export class GalleryContentComponent implements OnInit {

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

  constructor(private accountService: AccountService, private router: Router, private imageService: ImageService) {
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .subscribe(account => (this.extendedUser = account!));

      this.getPopularImages();
      this.getRecentImages();
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

  onLikeClick(image: IImage): void {
    this.imageService.likeImage(image.id).subscribe(() => {
        this.getPopularImages();
        this.getRecentImages();
    });
  }

  onCommentClick(image: IImage): void {
    this.router.navigate(['/image', image.id, 'view']);
  }

  trackId(index: number, item: IImage): number {
    return item.id;
  }

  prevSlide(): void {
    this.activeIndex = (this.activeIndex - 1 + this.carouselImages.length) % this.carouselImages.length;
  }

  nextSlide(): void  {
    this.activeIndex = (this.activeIndex + 1) % this.carouselImages.length;
  }
}
