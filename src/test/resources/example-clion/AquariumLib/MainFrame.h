/**
 * @file MainFrame.h
 * @author Charles B. Owen
 *
 * The top-level (main) frame of the application
 */
#ifndef _MAINFRAME_H_
#define _MAINFRAME_H_

#include <wx/config.h>

class AquariumView;

/**
 * The top-level (main) frame of the application
 */
class MainFrame : public wxFrame
{
public:
    MainFrame();
    ~MainFrame();

private:
    /// View class for our aquarium
    AquariumView *mAquariumView;

    void OnExit(wxCommandEvent& event);
    void OnAbout(wxCommandEvent&);
};

#endif //_MAINFRAME_H_
