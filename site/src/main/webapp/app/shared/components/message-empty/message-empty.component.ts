import { Component, Input } from "@angular/core";

@Component({
  selector: 'jhi-message-empty',
  templateUrl: './message-empty.component.html',
})
export class MessageEmptyComponent {
  @Input() translateMessage!: string;
}
