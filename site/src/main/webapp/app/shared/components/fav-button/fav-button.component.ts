import { Component, EventEmitter, Input, Output } from "@angular/core";

@Component({
  selector: 'jhi-fav-button',
  templateUrl: './fav-button.component.html'
})
export class FavButtonComponent {
  @Input() favourited: boolean | undefined = false;
  @Output() favClicked = new EventEmitter<boolean>();

  onLikeClick(): void {
    this.favClicked.emit();
  }
}
