export class NotificationFilter {
  notifyingId: number | null = null;

  map: Map<string, any> = new Map<string, any>();

  constructor(
    notifyingId?: number
    ) {
    this.notifyingId = notifyingId ?? null;
  }

  toMap(): any {
    if (this.notifyingId != null) {
      this.map.set('notifyingId.equals', this.notifyingId);
    }

    return this.map;
  }

  clear(): void {
    this.map = new Map<string, any>();
  }

  addFilter(key: string, value: string): void {
    if (key !== '' && value !== '') {
      this.toMap().set(key, value);
    }
  }
}
