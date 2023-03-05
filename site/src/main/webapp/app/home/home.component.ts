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
  account: IExtendedUser | null = null;

  popularImages: IImage[] = [];
  recentImages: IImage[] = [];

  predicate!: string;
  ascending!: boolean;

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
      .subscribe(account => (this.account = account));

    this.getPopularImages();
    this.getRecentImages();
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
      if (res.body) {
        this.popularImages = res.body;
      }
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

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
