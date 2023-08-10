import { RxStompService } from './rx-stomp.service';
import { myRxStompConfig } from '../../config/rx-stomp.config';
import { RxStomp } from '@stomp/rx-stomp/esm6/rx-stomp';

export function rxStompServiceFactory(): RxStomp {
  const rxStomp = new RxStompService();
  rxStomp.configure(myRxStompConfig);
  rxStomp.activate();
  return rxStomp;
}
