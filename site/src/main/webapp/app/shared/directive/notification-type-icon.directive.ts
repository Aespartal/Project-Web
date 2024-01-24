import { Directive, Input, ElementRef, Renderer2, OnInit } from '@angular/core';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

@Directive({
  selector: '[jhiNotificationTypeIcon]'
})
export class NotificationTypeIconDirective implements OnInit {
  @Input('jhiNotificationTypeIcon') notificationType!: NotificationType; // El tipo de notificación

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {}

  ngOnInit(): void {
    // Crea el elemento visual (corazón u otra cosa) según el tipo de notificación
    switch (this.notificationType) {
      case 'FOLLOW':
        this.createIcon('🧑'); // Nuevo seguidor
        break;
      case 'LIKE_IMAGE':
        this.createIcon('👍'); // Pulgar hacia arriba
        break;
      case 'LIKE_COMMENTARY':
        this.createIcon('👍'); // Pulgar hacia arriba (puedes usar un ícono diferente si lo deseas)
        break;
      case 'COMMENTARY':
        this.createIcon('💬'); // Bocadillo de diálogo
        break;
      default:
        // Si el tipo de notificación no coincide con ninguno de los casos anteriores, no se mostrará ningún ícono
        break;
    }
  }

  private createIcon(icon: string): void {
    const iconElement = this.renderer.createText(icon);
    this.renderer.appendChild(this.elementRef.nativeElement, iconElement);
  }
}
