/**
 * @file AquariumView.h
 * @author Charles B. Owen
 *
 * View class for our aquarium
 */

#ifndef AQUARIUM_AQUARIUMVIEW_H
#define AQUARIUM_AQUARIUMVIEW_H

#include "Aquarium.h"

class MainFrame;

/**
 * View class for our aquarium
 */
class AquariumView : public wxWindow {
public:
    AquariumView(MainFrame *parent);

    void OnPaint(wxPaintEvent& event);

private:
    /// The aquarium we are viewing
    Aquarium mAquarium;

    /// Any item we are currently dragging
    std::shared_ptr<Item> mGrabbedItem;

    void OnAddFishBetaFish(wxCommandEvent& event);
    void OnAddFishNemo(wxCommandEvent& event);
    void OnAddFishAngelFish(wxCommandEvent& event);
    void OnAddFishCarp(wxCommandEvent& event);

    void OnLeftDown(wxMouseEvent &event);
    void OnLeftUp(wxMouseEvent& event);
    void OnMouseMove(wxMouseEvent& event);
};

#endif //AQUARIUM_AQUARIUMVIEW_H
