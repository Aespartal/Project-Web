import { Component, EventEmitter, Output } from "@angular/core";

@Component({
  selector: 'jhi-comment-button',
  templateUrl: './comment-button.component.html'
})
export class CommentButtonComponent {

  @Output() commentClicked = new EventEmitter<boolean>();

  onCommentClick(): void {
    this.commentClicked.emit();
  }
}
