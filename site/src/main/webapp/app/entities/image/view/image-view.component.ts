import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImage } from '../image.model';
import { CommentaryService } from 'app/entities/commentary/service/commentary.service';
import { ICommentary, NewCommentary } from 'app/entities/commentary/commentary.model';
import { ImageService } from '../service/image.service';
import { AccountService } from 'app/core/auth/account.service';
import { IExtendedUser } from 'app/entities/extended-user/extended-user.model';

@Component({
  selector: 'jhi-image-view',
  templateUrl: './image-view.component.html',
  styleUrls: ['./image-view.component.scss'],
})
export class ImageViewComponent implements OnInit {

  image: IImage | null = null;
  commentaries: ICommentary[] = [];
  message!: string;
  extendedUser!: IExtendedUser;

  url = this.imageService.resourceUrl.concat('/base64/');

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected commentaryService: CommentaryService,
    protected imageService: ImageService,
    protected accountService: AccountService
    ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.accountService
        .getAuthenticationState()
        .subscribe(account => {
          this.extendedUser = account!
          this.image = image;
          this.loadCommentariesByImageId();
        });
    });
  }

  previousState(): void {
    window.history.back();
  }

  sendMessage(): void {
    if (!this.message || this.message === '') {
      return;
    }
    const commentary: NewCommentary = this.createNewCommentary();

    this.commentaryService.create(commentary).subscribe(
      ()=> {
        this.loadCommentariesByImageId();
      }
    )
  }

  deleteMessage(number: number): void {
    this.commentaryService.delete(number).subscribe(()=> {
        this.loadCommentariesByImageId();
    })
  }

  isCurrentUser(id: number): boolean {
    return this.extendedUser.id === id;
  }

  goBack(): void {
    this.previousState();
  }

  protected createNewCommentary(): NewCommentary {
    return {
      id: null,
      description: this.message,
      image: this.image
    }
  }

  protected loadCommentariesByImageId(): void {
    this.commentaryService.query({ 'imageId.equals': this.image?.id })
    .subscribe((res) => {
      this.commentaries = res.body!;
    });
  }
}
