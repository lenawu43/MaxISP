import Cookies from 'js-cookie'
export function getLang() {
  const PLAY_LANG = Cookies.get('PLAY_LANG');
  return require(`@/lang/${PLAY_LANG}`).data;
}