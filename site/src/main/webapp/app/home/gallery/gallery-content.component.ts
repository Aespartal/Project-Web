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

  predicate!: string;
  ascending!: boolean;

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    private accountService: AccountService,
    private router: Router,
    private imageService: ImageService
    ) {
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
    this.router.navigate(['/', 'image', image.id, 'photo-view']);
  }

  onImageClicked(image: IImage): void {
    this.router.navigate(['/', 'image', image.id, 'photo-view']);
  }

  trackId(index: number, item: IImage): number {
    return item.id;
  }
}
