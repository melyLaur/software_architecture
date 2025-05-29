import PopupContinue from "~/components/modals/PopupContinue.vue";


export const useModals = () => {
    const showPopupContinueModal = (message: string) => {
        const overlay = useOverlay()
        const modal = overlay.create(PopupContinue);

        return modal.open({ message: message})
    }

    return {
        showPopupContinueModal
    }
}