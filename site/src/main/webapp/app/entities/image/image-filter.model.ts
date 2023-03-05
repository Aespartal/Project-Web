/* eslint-disable @typescript-eslint/no-unnecessary-condition */
export class ImageFilter {
  id!: number | null;
  title!: string | null;
  description!: string | null;
  fileName!: string | null;
  isPrivate!: boolean;

  constructor() {
    this.reset();
  }

  toMap(): any {
    const filtros = new Map();

    if(this.id) {
      filtros.set('id.equals', this.id);
    }
    if(this.title) {
      filtros.set('title.contains', this.title);
    }
    if(this.description) {
      filtros.set('description.contains', this.description);
    }
    if(this.fileName) {
      filtros.set('fileName.contains', this.fileName);
    }

    return filtros;
  }

  private reset(): void {
    this.id = null;
    this.title = '';
    this.description = '';
    this.fileName = '';
    this.isPrivate = false;
  }
}
